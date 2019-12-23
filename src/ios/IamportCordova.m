/********* IamportCordova.m Cordova Plugin Implementation *******/

#import <Cordova/CDV.h>
#import "IamportViewController.h"
#import "IamportCertificationViewController.h"

@interface IamportCordova : CDVPlugin {
  // Member variables go here.
}

//- (void)coolMethod:(CDVInvokedUrlCommand*)command;
- (void)startActivity: (CDVInvokedUrlCommand*)command;
@end

@implementation IamportCordova

//- (void)coolMethod:(CDVInvokedUrlCommand*)command
//{
//    CDVPluginResult* pluginResult = nil;
//    NSString* echo = [command.arguments objectAtIndex:0];
//
//    if (echo != nil && [echo length] > 0) {
//        pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:echo];
//    } else {
//        pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR];
//    }
//
//    [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
//}

- (void)startActivity: (CDVInvokedUrlCommand*)command
{
    NSString* type = [command.arguments objectAtIndex:0];
    NSObject* params = [command.arguments objectAtIndex:1];
            
//    IamportViewController *iamportViewController = [[IamportViewController alloc] init];
    IamportViewController *iamportViewController = nil;
    if ([type isEqualToString:@"nice"]) {
        iamportViewController = [[IamportViewController alloc] init];
    } else if ([type isEqualToString:@"inicis"]) {
        iamportViewController = [[IamportViewController alloc] init];
    } else if ([type isEqualToString:@"certification"]) {
        iamportViewController = [[IamportCertificationViewController alloc] init];
    } else {
        iamportViewController = [[IamportViewController alloc] init];
    }
    
    iamportViewController.type = type;
    iamportViewController.params = params;

    [self.viewController presentViewController:iamportViewController animated:YES completion:nil];
}


@end
